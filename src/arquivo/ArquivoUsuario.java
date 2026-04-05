package arquivo;

import auxiliares.Arquivo;
import auxiliares.HashExtensivel;
import entidades.Usuario;

//Implementa o CRUD de usuário + índice por hash

public class ArquivoUsuario extends Arquivo<Usuario> {

    private HashExtensivel<EmailToID> indiceEmail;

    public ArquivoUsuario() throws Exception {
        super("usuarios", Usuario.class.getConstructor());
        indiceEmail = new HashExtensivel<>(EmailToID.class.getConstructor(), 4, ".\\dados\\usuarios\\indiceEmail.d.db", ".\\dados\\usuarios\\indiceEmail.c.db");
    }

    @Override
    public int create(Usuario u) throws Exception {
        if (readByEmail(u.getEmail()) != null)
            throw new Exception("E-mail já cadastrado: " + u.getEmail());
        int id = super.create(u);
        indiceEmail.create(new EmailToID(u.getEmail(), id));
        return id;
    }

    public Usuario readByEmail(String email) throws Exception {
        EmailToID par = indiceEmail.read(EmailToID.hash(email));
        if (par == null) return null;
        return super.read(par.getId());
    }

    @Override
    public boolean delete(int id) throws Exception {
        Usuario u = super.read(id);
        if (u == null) return false;
        if (super.delete(id))
            return indiceEmail.delete(EmailToID.hash(u.getEmail()));
        return false;
    }

    @Override
    public boolean update(Usuario novo) throws Exception {
        Usuario antigo = super.read(novo.getId());
        if (antigo == null) return false;

        boolean emailMudou = !novo.getEmail().equalsIgnoreCase(antigo.getEmail());
        if (emailMudou && readByEmail(novo.getEmail()) != null)
            throw new Exception("E-mail já em uso: " + novo.getEmail());

        if (super.update(novo)) {
            if (emailMudou) {
                indiceEmail.delete(EmailToID.hash(antigo.getEmail()));
                indiceEmail.create(new EmailToID(novo.getEmail(), novo.getId()));
            }
            return true;
        }
        return false;
    }
}
