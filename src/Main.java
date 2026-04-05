import java.util.Scanner;
import visao.MenuUsuarios;
import entidades.Usuario;

public class Main {
    public static void main(String[] args) {
        try {
            Scanner console = new Scanner(System.in);
            MenuUsuarios menuUsuarios = new MenuUsuarios();

            // Tela de Login
            Usuario usuarioLogado = menuUsuarios.telaInicial();
            if (usuarioLogado == null) {
                System.out.println("\nAté logo!");
                return;
            }

            // Menu principal
            String op;
            do {
                System.out.println("\n\nTP 1.0");
                System.out.println("--------------");
                System.out.println("> Início");
                System.out.println("\nOlá, " + usuarioLogado.getNome() + "!");
                System.out.println("\n(A) Meus dados");
                System.out.println("(B) Meus cursos");
                System.out.println("(C) Minhas inscrições");
                System.out.println("\n(S) Sair");
                System.out.print("\nOpção: ");
                op = console.nextLine().trim().toUpperCase();

                switch (op) {
                    case "A":
                        menuUsuarios.menu(usuarioLogado);
                        break;
                    case "B":
                    case "C":
                        System.out.println("\n[A ser implementado]");
                        break;
                    case "S":
                        System.out.println("\nAté logo, " + usuarioLogado.getNome() + "!");
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            } while (!op.equals("S"));

        } catch (Exception e) {
            System.out.println("Erro do sistema: " + e.getMessage());
            e.printStackTrace();
        }
    }
}