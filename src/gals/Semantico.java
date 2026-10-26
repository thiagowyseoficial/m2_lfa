package gals;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Semantico implements Constants {

    private final Stack<BigInteger> pilha = new Stack<>();
    private final Map<String, BigInteger> variaveis = new HashMap<>();
    private String destino;

    public void executeAction(int action, Token token) throws SemanticError {
        switch (action) {
            case 1:
                destino = token.getLexeme();
                break;

            case 2:
                variaveis.put(destino, pilha.pop());
                break;

            case 3:
                System.out.println(formatar(pilha.pop()));
                break;

            case 4:
                empilharOperacao('+');
                break;

            case 5:
                empilharOperacao('-');
                break;

            case 6:
                empilharOperacao('*');
                break;

            case 7:
                empilharOperacao('/');
                break;

            case 8:
                empilharOperacao('^');
                break;

            case 9:
                pilha.push(converterBinario(token.getLexeme()));
                break;

            case 10:
                String nome = token.getLexeme();
                if (!variaveis.containsKey(nome))
                    throw new SemanticError("Erro: variável '" + nome + "' não foi definida", token.getPosition());
                pilha.push(variaveis.get(nome));
                break;

            case 11:
                BigInteger valor = pilha.pop();
                if (valor.signum() <= 0)
                    throw new SemanticError("Erro: log de valor menor ou igual a zero", token.getPosition());
                // Para inteiros positivos, truncar log10 equivale a contar
                // os algarismos decimais menos um, sem arredondamento.
                pilha.push(BigInteger.valueOf(valor.toString().length() - 1));
                break;

            case 12:
                throw new SemanticError("Sinal negativo não permitido", token.getPosition());

            default:
                throw new SemanticError("Ação semântica desconhecida: #" + action, token != null ? token.getPosition() : 0);
        }
    }

    private void empilharOperacao(char operacao) throws SemanticError {
        if (pilha.size() < 2)
            throw new SemanticError("Erro: operação incompleta", 0);

        BigInteger b = pilha.pop();
        BigInteger a = pilha.pop();

        switch (operacao) {
            case '+':
                pilha.push(a.add(b));
                break;
            case '-':
                if (a.compareTo(b) < 0) throw new SemanticError("A operação produziu resultado negativo", 0);
                pilha.push(a.subtract(b));
                break;
            case '*':
                pilha.push(a.multiply(b));
                break;
            case '/':
                if (b.signum() == 0)
                    throw new SemanticError("Erro: divisão por zero", 0);
                // BigInteger.divide descarta a parte fracionária.
                pilha.push(a.divide(b));
                break;
            case '^':
                try {
                    pilha.push(a.pow(b.intValueExact()));
                } catch (ArithmeticException e) {
                    throw new SemanticError("Expoente fora dos limites suportados", 0);
                }
                break;
            default:
                throw new SemanticError("Operação inválida", 0);
        }
    }

    private BigInteger converterBinario(String valor) throws SemanticError {
        try {
            return new BigInteger(valor, 2);
        } catch (NumberFormatException e) {
            throw new SemanticError("Literal binário inválido", 0);
        }
    }

    private String formatar(BigInteger valor) {
        return valor.toString(2);
    }
}
