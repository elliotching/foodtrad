package kali.foodtrad;

/**
 * Created by elliotching on 21-Mar-17.
 */

class ObjectToken {
    protected String token;
    protected String username;
    protected boolean checked = false;
    ObjectToken(String token, String user){
        this.token = token;
        this.username = user;
    }
}
