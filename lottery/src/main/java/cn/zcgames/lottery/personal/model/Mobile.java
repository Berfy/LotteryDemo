package cn.zcgames.lottery.personal.model;


/**
 * 手机号
 *
 * @author NorthStar
 * @date 2018/11/19 10:38
 */
public class Mobile {
    String code;//国家码
    String numbers;//手机号

    public Mobile(String code, String numbers) {
        this.code = code;
        this.numbers = numbers;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumbers() {
        return numbers == null ? "" : numbers;
    }

    public void setNumbers(String numbers) {
        this.numbers = numbers;
    }
}
