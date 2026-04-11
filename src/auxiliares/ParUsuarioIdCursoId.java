package auxiliares;

import java.io.*;

public class ParUsuarioIdCursoId implements InterfaceArvoreBMais<ParUsuarioIdCursoId>, Comparable<ParUsuarioIdCursoId> {

    private int idUsuario;
    private int idCurso;
    private short TAMANHO = 8; // 4 bytes + 4 bytes

    public ParUsuarioIdCursoId() {
        this(-1, -1);
    }

    public ParUsuarioIdCursoId(int idUsuario, int idCurso) {
        this.idUsuario = idUsuario;
        this.idCurso = idCurso;
    }

    public int getIdUsuario() { return idUsuario; }
    public int getIdCurso() { return idCurso; }

    @Override
    public short size() {
        return TAMANHO;
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(idUsuario);
        dos.writeInt(idCurso);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        idUsuario = dis.readInt();
        idCurso = dis.readInt();
    }

    @Override
    public int compareTo(ParUsuarioIdCursoId obj) {
        if (this.idUsuario != obj.idUsuario)
            return Integer.compare(this.idUsuario, obj.idUsuario);
        return Integer.compare(this.idCurso, obj.idCurso);
    }

    @Override
    public ParUsuarioIdCursoId clone() {
        return new ParUsuarioIdCursoId(this.idUsuario, this.idCurso);
    }

    @Override
    public String toString() {
        return "Usuario: " + idUsuario + " | Curso: " + idCurso;
    }
}