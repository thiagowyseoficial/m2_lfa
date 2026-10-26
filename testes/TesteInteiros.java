import gals.*;
import java.io.*;
import java.math.BigInteger;

public class TesteInteiros {
    private static int total;
    private static void conferir(String codigo, String esperado) throws Exception {
        PrintStream original = System.out;
        ByteArrayOutputStream saida = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(saida));
            new Sintatico().parse(new Lexico(new StringReader(codigo)), new Semantico());
        } finally {
            System.setOut(original);
        }
        String obtido = saida.toString().replace("\r\n", "\n").trim();
        if (!obtido.equals(esperado))
            throw new AssertionError(codigo + ": esperado=" + esperado + ", obtido=" + obtido);
        total++;
    }
    private static void erro(String codigo, Class<?> tipo) throws Exception {
        try {
            new Sintatico().parse(new Lexico(new StringReader(codigo)), new Semantico());
        } catch (AnalysisError e) {
            if (!tipo.isInstance(e)) throw new AssertionError(codigo, e);
            total++;
            return;
        }
        throw new AssertionError("Deveria rejeitar: " + codigo);
    }
    public static void main(String[] args) throws Exception {
        conferir("A=10; B=11; B=111+A*B; Show(B);", "1101");
        conferir("Show(111/10);", "11");
        conferir("Show(1/10);", "0");
        conferir("A=111/10; Show(A*10);", "110");
        conferir("Show(log(1)); Show(log(100)); Show(log(1010));", "0\n0\n1");
        for (int n : new int[]{1, 2, 16, 100, 400}) {
            BigInteger limite = BigInteger.TEN.pow(n);
            conferir("Show(log("+limite.subtract(BigInteger.ONE).toString(2)+"));", Integer.toBinaryString(n-1));
            conferir("Show(log("+limite.toString(2)+"));", Integer.toBinaryString(n));
            conferir("Show(log("+limite.add(BigInteger.ONE).toString(2)+"));", Integer.toBinaryString(n));
        }
        conferir("Show(10^11^10);", BigInteger.valueOf(512).toString(2));
        conferir("A=1"+"0".repeat(53)+"; Show(A+1-A);", "1");
        conferir("Show("+"1".repeat(100)+");", "1".repeat(100));
        conferir("Show(10^1000000);", "1"+"0".repeat(64));
        erro("A=-1;", SyntacticError.class);
        erro("A=1-10;", SemanticError.class);
        erro("Show(1/0);", SemanticError.class);
        erro("Show(log(0));", SemanticError.class);
        erro("Show(A);", SemanticError.class);
        erro("A=12;", LexicalError.class);
        erro("Show(10^"+"1".repeat(32)+");", SemanticError.class);
        System.out.println(total + " verificações passaram.");
    }
}
