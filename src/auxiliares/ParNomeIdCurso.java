package auxiliares;

import java.io.*;

public class ParNomeIdCurso implements InterfaceArvoreBMais<ParNomeIdCurso>, Comparable<ParNomeIdCurso> {

    private String nome;
    private int idCurso;
    private short TAMANHO = 204; // 200 bytes para nome + 4 bytes para id

    public ParNomeIdCurso() {
        this("", -1);
    }

    public ParNomeIdCurso(String nome, int idCurso) {
        this.nome = nome;
        this.idCurso = idCurso;
    }

    public String getNome() { return nome; }
    public int getIdCurso() { return idCurso; }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        // Escreve nome com tamanho fixo
        byte[] nomeBytes = nome.getBytes("UTF-8");
        dos.writeInt(nomeBytes.length);
        dos.write(nomeBytes);
        
        // Preenche o restante com zeros se necessário
        int padding = 196 - nomeBytes.length; // 200 - 4 = 196
        for (int i = 0; i < padding; i++) {
            dos.writeByte(0);
        }
        
        dos.writeInt(idCurso);
        
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        int nomeLength = dis.readInt();
        byte[] nomeBytes = new byte[nomeLength];
        dis.read(nomeBytes);
        nome = new String(nomeBytes, "UTF-8");
        
        // Pula o padding
        dis.skip(196 - nomeLength);
        
        idCurso = dis.readInt();
    }

    @Override
    public int compareTo(ParNomeIdCurso obj) {
        int cmp = this.nome.compareToIgnoreCase(obj.nome);
        if (cmp != 0)
            return cmp;
        return Integer.compare(this.idCurso, obj.idCurso);
    }

    @Override
    public ParNomeIdCurso clone() {
        return new ParNomeIdCurso(this.nome, this.idCurso);
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " | ID: " + idCurso;
    }
}