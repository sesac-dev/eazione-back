package sesac.docshelper.domain.docs.dto.response;

public record DocsCountResponse (
    boolean isPassport,
    boolean isIdentityCard,
    int nomalDocsCnt
) { }
