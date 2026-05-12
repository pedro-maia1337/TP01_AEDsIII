package visao;

import arquivo.ArquivoCurso;
import arquivo.ArquivoInscricao;
import entidades.Curso;
import entidades.Inscricao;
import entidades.Usuario;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public class ControleInscricao {

    private ArquivoCurso arquivoCurso;
    private ArquivoInscricao arquivoInscricao;
    private VisaoCurso visao;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Quantidade de cursos exibidos por página na listagem
    private static final int ITENS_POR_PAGINA = 10;

    private static final Scanner console = new Scanner(System.in);

    public ControleInscricao() throws Exception {
        arquivoCurso     = new ArquivoCurso();
        arquivoInscricao = new ArquivoInscricao();
        visao            = new VisaoCurso();
    }

    // Menu principal de inscrições
    public void menu(Usuario usuarioLogado) {
        String opcao;
        do {
            try {
                exibirMenu(usuarioLogado);
                opcao = console.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "A":
                        buscarPorCodigo(usuarioLogado);
                        break;
                    case "B":
                        System.out.println("\n[A ser implementado]");
                        break;
                    case "C":
                        listarTodosCursos(usuarioLogado);
                        break;
                    case "R":
                        return;
                    default:
                        try {
                            Integer.parseInt(opcao);
                            System.out.println("\n[A ser implementado]");
                        } catch (NumberFormatException e) {
                            System.out.println("Opção inválida!");
                        }
                        break;
                }
            } catch (Exception e) {
                visao.mensagemErro("Erro do sistema: " + e.getMessage());
            }
        } while (true);
    }


    // Exibição do menu principal de inscrições
    private void exibirMenu(Usuario usuarioLogado) throws Exception {
        System.out.println("\n\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições");
        System.out.println("\nINSCRIÇÕES");

        // Busca inscrições do usuário e os cursos associados
        ArrayList<Inscricao> inscricoes = arquivoInscricao.readByUsuario(usuarioLogado.getId());

        if (inscricoes.isEmpty()) {
            System.out.println("Você não possui inscrições ativas.");
        } else {
            for (int i = 0; i < inscricoes.size(); i++) {
                Inscricao insc = inscricoes.get(i);
                Curso curso = arquivoCurso.read(insc.getIdCurso());

                if (curso == null) continue; // curso removido, ignora

                String labelStatus = getLabelStatus(curso);
                System.out.printf("(%d) %s - %s%s%n",
                        i + 1,
                        curso.getNome(),
                        curso.getDataInicio().format(FMT),
                        labelStatus
                );
            }
        }

        System.out.println("\n(A) Buscar curso por código");
        System.out.println("(B) Buscar curso por palavras-chave");
        System.out.println("(C) Listar todos os cursos");
        System.out.println("\n(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
    }

    // Busca por código NanoID  (opção A)

    private void buscarPorCodigo(Usuario usuarioLogado) {
        System.out.println("\nBUSCA POR CÓDIGO");
        System.out.println("================");
        System.out.print("Informe o código do curso (vazio para cancelar): ");

        String codigo = console.nextLine().trim();

        if (codigo.isEmpty()) {
            System.out.println("Operação cancelada.");
            return;
        }

        try {
            // Busca via HashExtensivel em ArquivoCurso — O(1)
            Curso curso = arquivoCurso.readByCodigo(codigo);

            if (curso == null) {
                System.out.println("Nenhum curso encontrado com o código: " + codigo);
                return;
            }

            // Exibe os dados do curso encontrado
            visao.mostraCurso(curso);

            System.out.println("\n[Inscrição a ser implementada]");

        } catch (Exception e) {
            visao.mensagemErro("Erro ao buscar curso: " + e.getMessage());
        }
    }

    // Listagem paginada de todos os cursos  (opção C)
    private void listarTodosCursos(Usuario usuarioLogado) {
        try {
            // readAll() retorna todos os cursos ordenados por nome via índice B+
            ArrayList<Curso> todos = arquivoCurso.readAll();

            // Remove cursos criados pelo próprio usuário logado
            ArrayList<Curso> terceiros = new ArrayList<>();
            for (Curso curso : todos) {
                if (curso.getIdUsuario() != usuarioLogado.getId()) {
                    terceiros.add(curso);
                }
            }

            if (terceiros.isEmpty()) {
                System.out.println("\nNenhum curso disponível no momento.");
                return;
            }

            int totalPaginas = (int) Math.ceil((double) terceiros.size() / ITENS_POR_PAGINA);
            int paginaAtual  = 1;

            String opcao;
            do {
                exibirPaginaCursos(terceiros, paginaAtual, totalPaginas);
                opcao = console.nextLine().trim().toUpperCase();

                switch (opcao) {
                    case "A": // Página anterior
                        if (paginaAtual > 1) {
                            paginaAtual--;
                        } else {
                            System.out.println("Você já está na primeira página.");
                        }
                        break;
                    case "B": // Próxima página
                        if (paginaAtual < totalPaginas) {
                            paginaAtual++;
                        } else {
                            System.out.println("Você já está na última página.");
                        }
                        break;
                    case "R":
                        return;
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } while (true);

        } catch (Exception e) {
            visao.mensagemErro("Erro ao listar cursos: " + e.getMessage());
        }
    }

    // Exibe uma página da listagem de cursos
    private void exibirPaginaCursos(ArrayList<Curso> cursos, int paginaAtual, int totalPaginas) {
        System.out.println("\n\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Minhas inscrições > Lista de cursos");
        System.out.printf("Página %d de %d%n", paginaAtual, totalPaginas);

        // Calcula os índices de início e fim da página atual
        int inicio = (paginaAtual - 1) * ITENS_POR_PAGINA;
        int fim    = Math.min(inicio + ITENS_POR_PAGINA, cursos.size());

        for (int i = inicio; i < fim; i++) {
            Curso curso = cursos.get(i);

            // O décimo item da página usa (0)
            int numeroExibido = (i - inicio + 1) % ITENS_POR_PAGINA;

            System.out.printf("(%d) %s - %s%n",
                    numeroExibido,
                    curso.getNome(),
                    curso.getDataInicio().format(FMT)
            );
        }

        // Exibe opções de navegação; omite as que não se aplicam
        System.out.println();
        if (paginaAtual > 1) {
            System.out.println("(A) Página anterior");
        }
        if (paginaAtual < totalPaginas) {
            System.out.println("(B) Próxima página");
        }
        System.out.println("(R) Retornar ao menu anterior");
        System.out.print("\nOpção: ");
    }

    // Retorna o label de status para exibição na lista de inscrições do usuário
    private String getLabelStatus(Curso curso) {
        switch (curso.getEstado()) {
            case Curso.ATIVO_SEM_INSCRICOES: return " (INSCRIÇÕES ENCERRADAS)";
            case Curso.CONCLUIDO:            return " (CONCLUÍDO)";
            case Curso.CANCELADO:            return " (CANCELADO)";
            default:                         return ""; // ATIVO_INSCRICOES não exibe label extra
        }
    }
}