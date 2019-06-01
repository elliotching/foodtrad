package kali.foodtrad;

/**
 * Created by elliotching on 12-May-17.
 */

class TokenListObject {
    final String username;
    final String token;
    String sendingResults = "";
    TokenListObject(String u, String t){
        username = u;
        token = t;
    }
}
