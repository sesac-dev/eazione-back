package sesac.docshelper.domain.member.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor // Lombok을 이용해서 멤버 변수를 모두 포함한 생성자를 강제 생성한다.
// -> 현업 비추천 방식이라고 함 (왜냐면 롬복 자체가 컴파일 단계에서 코드 -> 바이트코드 -> 기계어로 갈 때,
//    바이트 코드를 조작하여 생성자를 강제로 삽입함. AspectJ나 다른 바이트 코드 조작 의존성과 충돌 날 수 있음)

@Component               // 클래스 레벨에서 클래스 자체를 컴포넌트 스캔 과정을 거쳐 자동으로 Bean 객체로 등록시키는 어노테이션
// @Bean과의 차이점? -> @Bean은 매소드 레벨에 선언해서 매소드의 반환 객체를 Bean 객체로 등록

@Slf4j

// MultiPartFile 형태로 들어왔을 때 Uploader
public class S3MultiPartUploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 바이트 배열을 파일로 변환하는 메서드
    private File convertBytesToFile(byte[] bytes, String fileName) throws IOException {
        File file = new File(System.getProperty("java.io.tmpdir") + "/" + fileName+".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
        return file;
    }

    public String upload(byte[] bytes) throws IOException {
        // 바이트 배열을 파일로 변환
        File file = convertBytesToFile(bytes, "tempUploadFile");

        // 변환된 파일을 기존 업로드 메서드에 전달
        return upload(file);
    }


    // 1. MultipartFile -> File 변환 -> S3 업로드 -> URL 주소 반환
    public String upload(MultipartFile multipartFile) throws IOException {


        // (1) MultipartFile 타입은 S3에 업로드가 되지 않으므로, File 형태로 바꿔줘야 한다.
        File uploadFile = convert(multipartFile)
                .orElseThrow(() ->
                        // IllegalArgumentException은 메소드에 인수로 들어온 값이 잘못된 경우 발생하는 에러임.
                        new IllegalArgumentException("Converting Error...MultipartFile을 File로 변환하는 것에 실패하였습니다."));

        return upload(uploadFile);
    }

    // 2. File 객체를 받아서 S3에 업로드 후, 이미지의 주소("https://~")를 변환하는 메소드
    //    만약 그냥 파일로 들어온 경우에는 위의 함수를 거치지 않고 바로 S3로 올라갈 수 있도록 오버로딩을 사용
    private String upload (File uploadFile) {

        int a = (int) (Math.random() * 100000);
        // (1) File 이름은 유일무이하도록 설정
        String fileName = "userUpload" + "/" + a + uploadFile.getName();

        // (2) S3를 이용해 파일을 올리고 URL을 반환받기
        String uploadImageUrl = putS3(uploadFile, fileName);

        // (3) MultiPartFile 변환을 위해 쓰였던 임시 파일을 삭제
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }


    // 3. MultipartFile을 File 형태로 변환하는 매소드
    private Optional<File> convert(MultipartFile file) throws IOException {

        // (1) MultipartFile 객체는 사용자로부터 업로드 받은 MultipartFile 자체를 의미한다.
        //      File 객체는 서버 하드 디스크 내에 저장된 파일의 경로를 추상화한 객체 -> 실제 파일이나 디렉토리를 의미하지 않는다.
        //      우리는 MultipartFile의 전체 경로를 받아서 그것을 경로로 하는 File 객체를 하나 만들었다.
        //      Objects.requireNonNull()은 해당 ()안의 인수가 Null이면 error를 뱉는 매소드 이다.


        //      a) System.getProperty()는 현재 자바가 실행되는 운영체제에서 정보를 얻어올 수 있는 매소드
        //          "user.dir"을 인수로 넣으면, 현재 디렉토리 위치를 반환 한다.
        //          따라서 여기서는 dirPath를 현 서버 기준으로 맞게 다시 재조정 하는 것이다.

        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));

        // 마지막 파일 제거 과정까지 못 가고 오류가 난 경우, 아직 파일이 서버에 남아있음
        // 그래서 같은 파일을 올리려고 하면 에러가 나기 때문에 미리 삭제하고 가야한다.
        if (convertFile.exists() && !convertFile.delete()) {
            throw new IOException("이미 존재하는 파일을 삭제하는데 실패했습니다.");
        }

        //  createNewFile(): File 객체에 해당하는 경로에 빈 파일을 만들어낼 때 사용
        //  만약 해당 경로에 같은 이름을 가진 파일이 없으면 파일 생성 후 true를 반환 -> 여기서 이름은 dirPath(우리가 만든 경로)이다.
        //  만약 해당 경로에 같은 이름을 가진 파일이 있으면 false를 반환
        if(convertFile.createNewFile()) {
            //  b) 해당 경로에 있는 파일을 연다.
            try(FileOutputStream fos = new FileOutputStream(convertFile)) {

                // 해당 파일에 바이트 스트림 형태로 저장한다.
                fos.write(file.getBytes());
            }

            // Optional.of() => 들어오는 인수를 그대로 반환하는데, 인수가 null일 경우 NullPointer Error를 출력
            return Optional.of(convertFile);
        }
        return Optional.empty();
    }


    // 4. S3에 파일을 업로드하고 URL 을 받는 매소드
    private String putS3(File uploadFile, String fileName) {
        // 1) 파일을 S3에 넣고, Read 권한을 아무나 볼 수 있게(public)으로 설정
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));

        // 2) 파일을 올린 뒤 파일 이름으로 S3를 뒤져서 그 녀석의 URL 주소를 가져온다.
        return amazonS3Client.getUrl(bucket,fileName).toString();
    }


    // 5. URL 주소에서 파일 이름을 잘라내서, 해당 파일 이름을 가진 객체를 S3에서 삭제하는 매소드
    public void deleteS3(String filePath) throws Exception {
        try {
            // S3 이미지의 전체 주소에서 이름만 잘라내기
            String name = filePath.substring(56);

            try {
                amazonS3Client.deleteObject(bucket, name);
            } catch (AmazonServiceException e){
                log.info(e.getErrorMessage());
            }

        } catch (Exception exception){
            log.info(exception.getMessage());
        }

        // 성공 시
        log.info("[S3Uploader]: S3에 있는 {} 파일 삭제 ", filePath);
    }

    // 6. MultipartFile을 File로 변환하기 위해 임시로 만들었던 파일을 삭제하는 매소드
    private void removeNewFile (File targetFile) {
        if(targetFile.delete()){
            log.info("[S3Uploader]: 임시 파일 삭제 성공");
            return;
        }
        log.info("[S3Uploader]: 파일 삭제 실패");
    }
}