/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab7;

/**
 *
 * @author Marwan
 */
public class user {
    
    private String userId;
    private String username;
    private String email;
    private String passhash;
    private String role; // student or instructor // 


public user (String userId , String username , String email , String passhash , String role)
{
this.userId = userId ;
this.username = username ;
this.email = email ;
this.passhash = passhash ;
this.role = role ;
}

public String getUserName ()
{ return username ; }

public String getUserId ()
{ return userId ; }

public String getEmail ()
{ return email ; }

public String getPasswordHash ()
{ return passhash ; }

public String getRole ()
{ return role ; }

                   }





