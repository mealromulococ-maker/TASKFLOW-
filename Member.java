
package model;

public class Member {

    private int memberId;
    private String memberName;
    private String email;
    private String role;
    private String status = "Active";

    public Member() {
    }

    public Member(int memberId, String memberName,
                  String email, String role) {

        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.role = role;
    }

    public Member(int memberId, String memberName,
                  String email, String role, String status) {

        this.memberId = memberId;
        this.memberName = memberName;
        this.email = email;
        this.role = role;
        this.status = status;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
