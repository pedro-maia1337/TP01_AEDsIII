package arquivo;

import auxiliares.Arquivo;
import auxiliares.HashExtensivel;
import auxiliares.ArvoreBMais;
import auxiliares.ParUsuarioIdCursoId;
import auxiliares.ParNomeIdCurso;
import entidades.Curso;
import java.util.ArrayList;

public class ArquivoCurso extends Arquivo<Curso> {

    private HashExtensivel<CodigoToID> indiceCodigo;
    private ArvoreBMais<ParUsuarioIdCursoId> indiceUsuarioCurso;
    private ArvoreBMais<ParNomeIdCurso> indiceNome;

    public ArquivoCurso() throws Exception {
        super("cursos", Curso.class.getConstructor());
        indiceCodigo = new HashExtensivel<>(CodigoToID.class.getConstructor(), 4, 
            ".\\dados\\cursos\\indiceCodigo.d.db", ".\\dados\\cursos\\indiceCodigo.c.db");
        indiceUsuarioCurso = new ArvoreBMais<>(ParUsuarioIdCursoId.class.getConstructor(), 5,
            ".\\dados\\cursos\\indiceUsuarioCurso.db");
        indiceNome = new ArvoreBMais<>(ParNomeIdCurso.class.getConstructor(), 5,
            ".\\dados\\cursos\\indiceNome.db");
    }

    @Override
    public int create(Curso c) throws Exception {
        // Verifica se já existe um curso com o mesmo código compartilhável
        if (readByCodigo(c.getCodigoCompartilhavel()) != null) {
            throw new Exception("Código compartilhável já existe: " + c.getCodigoCompartilhavel());
        }
        
        int id = super.create(c);
        indiceCodigo.create(new CodigoToID(c.getCodigoCompartilhavel(), id));
        indiceUsuarioCurso.create(new ParUsuarioIdCursoId(c.getIdUsuario(), id));
        indiceNome.create(new ParNomeIdCurso(c.getNome(), id));
        return id;
    }

    public Curso readByCodigo(String codigo) throws Exception {
        CodigoToID par = indiceCodigo.read(CodigoToID.hash(codigo));
        if (par == null) return null;
        return super.read(par.getId());
    }

    @Override
    public boolean delete(int id) throws Exception {
        Curso c = super.read(id);
        if (c == null) return false;
        
        if (super.delete(id)) {
            indiceCodigo.delete(CodigoToID.hash(c.getCodigoCompartilhavel()));
            indiceUsuarioCurso.delete(new ParUsuarioIdCursoId(c.getIdUsuario(), id));
            indiceNome.delete(new ParNomeIdCurso(c.getNome(), id));
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Curso novo) throws Exception {
        Curso antigo = super.read(novo.getId());
        if (antigo == null) return false;

        boolean codigoMudou = !novo.getCodigoCompartilhavel().equals(antigo.getCodigoCompartilhavel());
        if (codigoMudou && readByCodigo(novo.getCodigoCompartilhavel()) != null) {
            throw new Exception("Código compartilhável já em uso: " + novo.getCodigoCompartilhavel());
        }

        if (super.update(novo)) {
            if (codigoMudou) {
                indiceCodigo.delete(CodigoToID.hash(antigo.getCodigoCompartilhavel()));
                indiceCodigo.create(new CodigoToID(novo.getCodigoCompartilhavel(), novo.getId()));
            }
            
            // Atualiza o relacionamento usuário-curso se mudou o usuário
            if (novo.getIdUsuario() != antigo.getIdUsuario()) {
                indiceUsuarioCurso.delete(new ParUsuarioIdCursoId(antigo.getIdUsuario(), novo.getId()));
                indiceUsuarioCurso.create(new ParUsuarioIdCursoId(novo.getIdUsuario(), novo.getId()));
            }
            
            // Atualiza índice de nome se mudou o nome
            if (!novo.getNome().equals(antigo.getNome())) {
                indiceNome.delete(new ParNomeIdCurso(antigo.getNome(), novo.getId()));
                indiceNome.create(new ParNomeIdCurso(novo.getNome(), novo.getId()));
            }
            
            return true;
        }
        return false;
    }
    
    // Busca cursos de um usuário específico
    public ArrayList<Curso> readByUsuario(int idUsuario) throws Exception {
        ArrayList<Curso> cursos = new ArrayList<>();
        
        // Cria uma chave de busca com o ID do usuário
        ParUsuarioIdCursoId chave = new ParUsuarioIdCursoId(idUsuario, 0);
        ArrayList<ParUsuarioIdCursoId> pares = indiceUsuarioCurso.read(chave);
        
        // Adiciona também uma busca sem especificar o ID do curso para pegar todos
        ArrayList<ParUsuarioIdCursoId> todosPares = indiceUsuarioCurso.read(null);
        
        for (ParUsuarioIdCursoId par : todosPares) {
            if (par.getIdUsuario() == idUsuario) {
                Curso curso = super.read(par.getIdCurso());
                if (curso != null && curso.getIdUsuario() == idUsuario) {
                    cursos.add(curso);
                }
            }
        }
        
        return cursos;
    }
    
    // Busca cursos ordenados por nome
    public ArrayList<Curso> readAll() throws Exception {
        ArrayList<Curso> cursos = new ArrayList<>();
        
        // Busca todos os cursos via índice de nome
        ArrayList<ParNomeIdCurso> pares = indiceNome.read(null);
        
        for (ParNomeIdCurso par : pares) {
            Curso curso = super.read(par.getIdCurso());
            if (curso != null) {
                cursos.add(curso);
            }
        }
        
        return cursos;
    }
}