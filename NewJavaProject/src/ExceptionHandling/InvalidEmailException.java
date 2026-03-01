/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ExceptionHandling;

/**
 *
 * @author Nazifah
 */
public class InvalidEmailException extends ValidationException {
    
    public InvalidEmailException(String message) {
        super(message);
    }
}
