package eu.mikart.cleanrtp.references.file;

public class Files {
    private final FileLanguage langFile = new FileLanguage();
    private final FileOther basics = new FileOther();

    public FileLanguage getLang() {
        return langFile;
    }

    public FileOther.Filetype getType(FileOther.Filetype type) {
        return basics.types.get(basics.types.indexOf(type));
    }

    public void loadAll() {
        basics.load();
        langFile.load();
    }
}

