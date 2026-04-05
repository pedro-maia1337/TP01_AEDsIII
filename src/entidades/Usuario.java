package entidades;

import auxiliares.Registro;
import java.io.*;

public class Usuario implements Registro {

    private int    id;
    private String nome;
    private String email;
    private int    hashSenha;
    private String perguntaSecreta;
    private int    hashRespostaSecreta;

    public static final int MAX_EMAIL    = 100;
    public static final int MAX_NOME     = 100;
    public static final int MAX_PERGUNTA = 150;

    //Construtor
    public Usuario() {
        this(-1, "", "", 0, "", 0);
    }

    public Usuario(String nome, String email, String senha,
                   String pergunta, String resposta) {
        this(-1, nome, email, senha.hashCode(),
             pergunta, resposta.toLowerCase().trim().hashCode());
    }

    public Usuario(int id, String nome, String email, int hashSenha,
                   String perguntaSecreta, int hashRespostaSecreta) {
        this.id                  = id;
        this.nome                = nome;
        this.email               = email;
        this.hashSenha           = hashSenha;
        this.perguntaSecreta     = perguntaSecreta;
        this.hashRespostaSecreta = hashRespostaSecreta;
    }

    @Override public int  getId()       { return id; }
    @Override public void setId(int id) { this.id = id; }

    //Getters e Setters
    public String getNome()             { return nome; }
    public String getEmail()            { return email; }
    public int    getHashSenha()        { return hashSenha; }
    public String getPerguntaSecreta()  { return perguntaSecreta; }
    public int    getHashResposta()     { return hashRespostaSecreta; }

    public void setNome(String nome)             { this.nome  = nome; }
    public void setEmail(String email)           { this.email = email; }
    public void setSenha(String novaSenha)       { this.hashSenha = novaSenha.hashCode(); }
    public void setHashSenha(int h)              { this.hashSenha = h; }
    public void setPerguntaSecreta(String p)     { this.perguntaSecreta = p; }
    public void setRespostaSecreta(String r)     { this.hashRespostaSecreta = r.toLowerCase().trim().hashCode(); }
    public void setHashResposta(int h)           { this.hashRespostaSecreta = h; }

    public boolean verificaSenha(String senha)   { return this.hashSenha == senha.hashCode(); }
    public boolean verificaResposta(String resp) {
        return this.hashRespostaSecreta == resp.toLowerCase().trim().hashCode();
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeUTF(nome);
        dos.writeUTF(email);
        dos.writeInt(hashSenha);
        dos.writeUTF(perguntaSecreta);
        dos.writeInt(hashRespostaSecreta);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        id                  = dis.readInt();
        nome                = dis.readUTF();
        email               = dis.readUTF();
        hashSenha           = dis.readInt();
        perguntaSecreta     = dis.readUTF();
        hashRespostaSecreta = dis.readInt();
    }

    @Override
    public String toString() {
        return "\nID: " + id +
               "\nNome: " + nome +
               "\nE-mail: " + email +
               "\nPergunta: " + perguntaSecreta;
    }
}
