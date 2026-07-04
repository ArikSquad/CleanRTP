package eu.mikart.cleanrtp.references.file;

public class Files {
    private final FileLanguage langFile = new FileLanguage();

    public FileLanguage getLang() {
        return langFile;
    }


    public void loadAll() {
        langFile.load();
    }
}

