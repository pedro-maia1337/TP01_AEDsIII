package arquivo;

import auxiliares.Arquivo;
import auxiliares.ArvoreBMais;
import auxiliares.ParUsuarioIdInscricaoId;
import entidades.Inscricao;
import java.util.ArrayList;

public class ArquivoInscricao extends Arquivo<Inscricao> {

    // Índice B+: idUsuario → idInscrição  (buscar "minhas inscrições")
    private ArvoreBMais<ParUsuarioIdInscricaoId> indiceUsuarioInscricao;

    public ArquivoInscricao() throws Exception {
        super("inscricoes", Inscricao.class.getConstructor());
        indiceUsuarioInscricao = new ArvoreBMais<>(
            ParUsuarioIdInscricaoId.class.getConstructor(), 5,
            ".\\dados\\inscricoes\\indiceUsuarioInscricao.db"
        );
    }

    @Override
    public int create(Inscricao i) throws Exception {
        int id = super.create(i);
        indiceUsuarioInscricao.create(new ParUsuarioIdInscricaoId(i.getIdUsuario(), id));
        return id;
    }

    @Override
    public boolean delete(int id) throws Exception {
        Inscricao i = super.read(id);
        if (i == null) return false;

        if (super.delete(id)) {
            indiceUsuarioInscricao.delete(
                new ParUsuarioIdInscricaoId(i.getIdUsuario(), id)
            );
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Inscricao nova) throws Exception {
        Inscricao antiga = super.read(nova.getId());
        if (antiga == null) return false;

        if (super.update(nova)) {
            // Se o usuário mudou, atualiza o índice
            if (nova.getIdUsuario() != antiga.getIdUsuario()) {
                indiceUsuarioInscricao.delete(
                    new ParUsuarioIdInscricaoId(antiga.getIdUsuario(), nova.getId())
                );
                indiceUsuarioInscricao.create(
                    new ParUsuarioIdInscricaoId(nova.getIdUsuario(), nova.getId())
                );
            }
            return true;
        }
        return false;
    }

    // Busca todas as inscrições de um usuário, percorrendo o índice B+
    public ArrayList<Inscricao> readByUsuario(int idUsuario) throws Exception {
        ArrayList<Inscricao> inscricoes = new ArrayList<>();

        ArrayList<ParUsuarioIdInscricaoId> todosPares =
            indiceUsuarioInscricao.read(null);

        for (ParUsuarioIdInscricaoId par : todosPares) {
            if (par.getIdUsuario() == idUsuario) {
                Inscricao inscricao = super.read(par.getIdInscricao());
                if (inscricao != null) {
                    inscricoes.add(inscricao);
                }
            }
        }

        return inscricoes;
    }

    // Verifica se o usuário já está inscrito em determinado curso
    public boolean existeInscricao(int idUsuario, int idCurso) throws Exception {
        ArrayList<Inscricao> inscricoes = readByUsuario(idUsuario);
        for (Inscricao i : inscricoes) {
            if (i.getIdCurso() == idCurso && i.getEstado() == Inscricao.ATIVA) {
                return true;
            }
        }
        return false;
    }
}
