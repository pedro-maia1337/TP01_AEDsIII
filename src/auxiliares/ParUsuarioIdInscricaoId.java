package auxiliares;

import java.io.*;

// Par (idUsuario, idInscrição) para o índice B+ de inscrições por usuário.

public class ParUsuarioIdInscricaoId implements InterfaceArvoreBMais<ParUsuarioIdInscricaoId>,
                                                Comparable<ParUsuarioIdInscricaoId> {

    private int idUsuario;
    private int idInscricao;
    private short TAMANHO = 8; // 4 bytes + 4 bytes

    public ParUsuarioIdInscricaoId() {
        this(-1, -1);
    }

    public ParUsuarioIdInscricaoId(int idUsuario, int idInscricao) {
        this.idUsuario   = idUsuario;
        this.idInscricao = idInscricao;
    }

    public int getIdUsuario()   { return idUsuario; }
    public int getIdInscricao() { return idInscricao; }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeInt(idInscricao);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idUsuario   = dis.readInt();
        idInscricao = dis.readInt();
    }

    // Ordena por idUsuario; desempata por idInscricao
    @Override
    public int compareTo(ParUsuarioIdInscricaoId obj) {
        if (this.idUsuario != obj.idUsuario)
            return Integer.compare(this.idUsuario, obj.idUsuario);
        return Integer.compare(this.idInscricao, obj.idInscricao);
    }

    @Override
    public ParUsuarioIdInscricaoId clone() {
        return new ParUsuarioIdInscricaoId(this.idUsuario, this.idInscricao);
    }

    @Override
    public String toString() {
        return "Usuario: " + idUsuario + " | Inscrição: " + idInscricao;
    }
}
