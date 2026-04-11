package visao;

import arquivo.ArquivoCurso;
import arquivo.ArquivoUsuario;
import entidades.Curso;
import entidades.Usuario;
import java.util.ArrayList;

public class ControleCurso {

    private ArquivoCurso arquivoCurso;
    private ArquivoUsuario arquivoUsuario;
    private VisaoCurso visao;

    public ControleCurso() throws Exception {
        arquivoCurso = new ArquivoCurso();
        arquivoUsuario = new ArquivoUsuario();
        visao = new VisaoCurso();
    }

    public void menu(Usuario usuarioLogado) {
        String opcao;
        do {
            try {
                exibirMenuPrincipal(usuarioLogado);
                opcao = visao.lerOpcaoTexto();

                switch (opcao) {
                    case "A":
                        criarCurso(usuarioLogado);
                        break;
                    case "R":
                        return; // Retorna ao menu anterior
                    default:
                        // Tenta interpretar como número (seleção de curso)
                        try {
                            int numeroCurso = Integer.parseInt(opcao);
                            selecionarCurso(usuarioLogado, numeroCurso);
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

    private void exibirMenuPrincipal(Usuario usuarioLogado) throws Exception {
        System.out.println("\n\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus cursos");
        System.out.println();

        // Busca cursos do usuário ordenados por nome
        ArrayList<Curso> cursos = arquivoCurso.readByUsuario(usuarioLogado.getId());
        cursos.sort((c1, c2) -> c1.getNome().compareToIgnoreCase(c2.getNome()));

        System.out.println("CURSOS");
        if (cursos.isEmpty()) {
            System.out.println("Nenhum curso cadastrado.");
        } else {
            for (int i = 0; i < cursos.size(); i++) {
                Curso curso = cursos.get(i);
                System.out.printf("(%d) %s - %02d/%02d/%d%n", 
                    i + 1, 
                    curso.getNome(), 
                    curso.getDataInicio().getDayOfMonth(),
                    curso.getDataInicio().getMonthValue(),
                    curso.getDataInicio().getYear());
            }
        }

        System.out.println("\n(A) Novo curso");
        System.out.println("(R) Retornar ao menu anterior");
    }

    private void criarCurso(Usuario usuarioLogado) {
        try {
            Curso novoCurso = visao.leCurso(usuarioLogado.getId());
            int id = arquivoCurso.create(novoCurso);
            visao.mensagemSucesso("Curso cadastrado com ID " + id);
        } catch (Exception e) {
            visao.mensagemErro("Não foi possível cadastrar o curso: " + e.getMessage());
        }
    }

    private void selecionarCurso(Usuario usuarioLogado, int numeroCurso) throws Exception {
        ArrayList<Curso> cursos = arquivoCurso.readByUsuario(usuarioLogado.getId());
        cursos.sort((c1, c2) -> c1.getNome().compareToIgnoreCase(c2.getNome()));

        if (numeroCurso < 1 || numeroCurso > cursos.size()) {
            System.out.println("Número de curso inválido!");
            return;
        }

        Curso curso = cursos.get(numeroCurso - 1);
        menuCurso(curso);
    }

    private void menuCurso(Curso curso) {
        String opcao;
        do {
            try {
                exibirMenuCurso(curso);
                opcao = visao.lerOpcaoTexto();

                switch (opcao) {
                    case "A":
                        System.out.println("\n[A ser implementado no TP2]");
                        break;
                    case "B":
                        alterarCurso(curso);
                        break;
                    case "C":
                        encerrarInscricoes(curso);
                        break;
                    case "D":
                        concluirCurso(curso);
                        break;
                    case "E":
                        cancelarCurso(curso);
                        break;
                    case "R":
                        return; // Retorna ao menu anterior
                    default:
                        System.out.println("Opção inválida!");
                        break;
                }
            } catch (Exception e) {
                visao.mensagemErro("Erro do sistema: " + e.getMessage());
            }
        } while (true);
    }

    private void exibirMenuCurso(Curso curso) {
        System.out.println("\n\nEntrePares 1.0");
        System.out.println("--------------");
        System.out.println("> Início > Meus Cursos > " + curso.getNome());
        
        visao.mostraCurso(curso);
        
        System.out.println("\n(A) Gerenciar inscritos no curso");
        System.out.println("(B) Corrigir dados do curso");
        
        if (curso.getEstado() == Curso.ATIVO_INSCRICOES) {
            System.out.println("(C) Encerrar inscrições");
        }
        
        if (curso.getEstado() == Curso.ATIVO_INSCRICOES || curso.getEstado() == Curso.ATIVO_SEM_INSCRICOES) {
            System.out.println("(D) Concluir curso");
        }
        
        if (curso.getEstado() != Curso.CANCELADO && curso.getEstado() != Curso.CONCLUIDO) {
            System.out.println("(E) Cancelar curso");
        }
        
        System.out.println("\n(R) Retornar ao menu anterior");
    }

    private void alterarCurso(Curso curso) {
        try {
            Curso cursoAlterado = visao.alteraCurso(curso);
            if (arquivoCurso.update(cursoAlterado)) {
                visao.mensagemSucesso("Curso alterado");
            } else {
                visao.mensagemErro("Não foi possível alterar o curso");
            }
        } catch (Exception e) {
            visao.mensagemErro("Erro ao alterar curso: " + e.getMessage());
        }
    }

    private void encerrarInscricoes(Curso curso) {
        try {
            if (curso.getEstado() == Curso.ATIVO_INSCRICOES) {
                curso.setEstado(Curso.ATIVO_SEM_INSCRICOES);
                if (arquivoCurso.update(curso)) {
                    visao.mensagemSucesso("Inscrições encerradas para o curso");
                } else {
                    visao.mensagemErro("Não foi possível encerrar as inscrições");
                }
            } else {
                visao.mensagemErro("O curso não está recebendo inscrições");
            }
        } catch (Exception e) {
            visao.mensagemErro("Erro ao encerrar inscrições: " + e.getMessage());
        }
    }

    private void concluirCurso(Curso curso) {
        try {
            if (curso.getEstado() == Curso.ATIVO_INSCRICOES || curso.getEstado() == Curso.ATIVO_SEM_INSCRICOES) {
                curso.setEstado(Curso.CONCLUIDO);
                if (arquivoCurso.update(curso)) {
                    visao.mensagemSucesso("Curso marcado como concluído");
                } else {
                    visao.mensagemErro("Não foi possível concluir o curso");
                }
            } else {
                visao.mensagemErro("O curso não pode ser concluído no estado atual");
            }
        } catch (Exception e) {
            visao.mensagemErro("Erro ao concluir curso: " + e.getMessage());
        }
    }

    private void cancelarCurso(Curso curso) {
        try {
            // Aqui a gente pode colocar a verificacao se tem inscrições no TP2
            // Por enquanto apenas cancela o curso
            
            if (visao.confirmaExclusao(curso)) {
                curso.setEstado(Curso.CANCELADO);
                if (arquivoCurso.update(curso)) {
                    visao.mensagemSucesso("Curso cancelado");
                } else {
                    visao.mensagemErro("Não foi possível cancelar o curso");
                }
            }
        } catch (Exception e) {
            visao.mensagemErro("Erro ao cancelar curso: " + e.getMessage());
        }
    }
}