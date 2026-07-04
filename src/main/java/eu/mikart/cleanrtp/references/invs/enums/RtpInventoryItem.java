package eu.mikart.cleanrtp.references.invs.enums;

public enum RtpInventoryItem {
    NORMAL("paper", 1),
    BACK("book", 1, "Back", 0);

    public String item, name;
    public int amt, slot = -1;

    RtpInventoryItem(String item, int amt) {
        this.item = item;
        this.amt = amt;
    }

    RtpInventoryItem(String item, int amt, String name, int slot) {
        this.item = item;
        this.amt = amt;
        this.name = name;
        this.slot = slot;
    }
}
