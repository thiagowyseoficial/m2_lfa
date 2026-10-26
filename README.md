# Trabalho M2 — Linguagens Formais e Autômatos

Interpretador de expressões sobre inteiros binários sem sinal, com analisadores gerados pelo GALs e ações implementadas em Java.

## Integrantes

- João Marcelo
- Thiago Wyse

## Conteúdo

- `trabalho_m2.gals`: projeto atualizado para abrir no GALs/WebGALs.
- `src/gals/`: fontes Java finais, incluindo o interpretador em `Semantico.java`.
- `testes/TesteInteiros.java`: 31 verificações automáticas.

## Executar pelo terminal

É necessário um JDK com `java` e `javac` disponíveis no PATH. No PowerShell, abra esta pasta e execute:

```powershell
New-Item -ItemType Directory -Force bin | Out-Null
javac -encoding UTF-8 -d bin (Get-ChildItem src/gals -Filter '*.java').FullName
java -cp bin gals.Main
```

O programa de exemplo fica na variável `codigo` de `src/gals/Main.java`. Para testar outro programa, altere essa string e compile novamente.

Saída esperada do Main incluído:

```text
1101
1
11
```

A primeira linha é a saída do exemplo do enunciado. As outras demonstram logaritmo e expressão com divisão e potência.

## Executar no IntelliJ

Abra a pasta do projeto como projeto Java, selecione um JDK e marque a pasta `src` como Sources Root. Execute a classe `gals.Main`. Os arquivos já declaram `package gals`.

## Testes automáticos

Após compilar os fontes, execute no PowerShell:

```powershell
javac -encoding UTF-8 -cp bin -d bin testes/TesteInteiros.java
java -cp bin TesteInteiros
```

Resultado esperado: `31 verificações passaram.` Os testes incluem as seis operações, atribuição, saída binária, precedência, precisão, valores grandes, limites do log e erros léxicos, sintáticos e semânticos.

## Regras adotadas

- Literais contêm apenas `0` e `1`; a saída também é binária.
- Valores são inteiros não negativos, incluindo zero, armazenados como `BigInteger`.
- Subtrações que produzam negativos são rejeitadas. Sinal negativo unário não faz parte da gramática.
- Divisão descarta o resto: `111 / 10` resulta em `11` (7 / 2 resulta em 3).
- `log` usa base 10 e descarta a fração. Para inteiros positivos, o cálculo exato corresponde ao número de algarismos decimais menos um. Não utiliza aproximação em ponto flutuante.
- Divisão por zero, log de zero e variáveis não definidas geram erro semântico.
- Potência associa à direita. Multiplicação e divisão precedem soma e subtração; parênteses alteram a ordem.
- A potência usa expoente inteiro representável como `int` não negativo; valores acima desse limite são rejeitados. O tamanho dos cálculos também depende da memória disponível. `0 ^ 0` resulta em 1, conforme `BigInteger.pow`.
- Identificadores e palavras reservadas distinguem maiúsculas de minúsculas: `Show` e `log`.

O enunciado não especifica a base do log, a política de truncamento nem explicitamente a base da saída. As escolhas acima foram adotadas no projeto e ainda não foram confirmadas pelo professor.

## Alterações no GALs

Abra `trabalho_m2.gals` para consultar tokens, gramática e ações `#1` a `#11`. Se gerar novamente os analisadores, preserve o `Semantico.java` desta entrega: o arquivo semântico exportado pelo gerador é apenas um modelo e não contém a implementação dos cálculos.

## Conferência antes da entrega

Compile, execute `gals.Main` e rode os testes. A compilação e os testes foram conferidos por terminal; a execução pela interface do IntelliJ deve ser feita no ambiente de apresentação.
