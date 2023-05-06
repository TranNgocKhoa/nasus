package io.github.tranngockhoa.nasus.bean.wire;

public class SmartPhoneWithAdapter {
    private final SmartPhone smartPhone;
    public final Adapter adapter;

    public SmartPhoneWithAdapter(SmartPhone smartPhone, Adapter adapter) {
        this.smartPhone = smartPhone;
        this.adapter = adapter;
    }

    public void check() {
        System.out.println("SmartPhoneWithAdapter: ");
        System.out.println("SmartPhone: " + smartPhone);
        System.out.println("Adapter: " + adapter);
        System.out.println();
    }
}
