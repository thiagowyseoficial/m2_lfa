package gals;

import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        String codigo =
              "A = 10;\n"
            + "B = 11;\n"
            + "B = 111 + A * B;\n"
            + "Show ( B );\n"
            + "Show ( log(B) );\n"
            + "Show ( (B - 1) / 10 ^ 10 );\n";

        Lexico lexico = new Lexico();
        Sintatico sintatico = new Sintatico();
        Semantico semantico = new Semantico();
        lexico.setInput(new StringReader(codigo));

        try {
            sintatico.parse(lexico, semantico);
        } catch (LexicalError e) {
            System.err.println("Erro léxico: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        } catch (SyntacticError e) {
            System.err.println("Erro sintático: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        } catch (SemanticError e) {
            System.err.println("Erro semântico: " + e.getMessage() + " (posição " + e.getPosition() + ")");
        }
    }
}
