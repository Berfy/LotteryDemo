package cn.zcgames.lottery.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by admin on 2017/5/12.
 */
@Entity
public class BankCardBean {

    @Id(autoincrement = true)
    private Long id;
    private String icon;
    private String name;
    private String title;
    private String cardid;
    private String bankname;

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private int logoId;

    @Generated(hash = 1423730895)
    public BankCardBean(Long id, String icon, String name, String title,
            String cardid, String bankname, int logoId) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.title = title;
        this.cardid = cardid;
        this.bankname = bankname;
        this.logoId = logoId;
    }

    @Generated(hash = 52061025)
    public BankCardBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLogoId() {
        return logoId;
    }

    public void setLogoId(int logoId) {
        this.logoId = logoId;
    }
}
