package entidades;

import auxiliares.Registro;
import java.io.*;
import java.time.LocalDate;

public class Inscricao implements Registro {

    private int id;
    private int idUsuario;  // Chave estrangeira - aluno inscrito
    private int idCurso;    // Chave estrangeira - curso da inscrição
    private LocalDate dataInscricao;
    private int estado; // 0=ativa, 1=cancelada

    // Constantes para estados
    public static final int ATIVA    = 0;
    public static final int CANCELADA = 1;

    public Inscricao() {
        this(-1, -1, -1, LocalDate.now(), ATIVA);
    }

    public Inscricao(int idUsuario, int idCurso) {
        this(-1, idUsuario, idCurso, LocalDate.now(), ATIVA);
    }

    public Inscricao(int id, int idUsuario, int idCurso, LocalDate dataInscricao, int estado) {
        this.id             = id;
        this.idUsuario      = idUsuario;
        this.idCurso        = idCurso;
        this.dataInscricao  = dataInscricao;
        this.estado         = estado;
    }

    @Override public int  getId()       { return id; }
    @Override public void setId(int id) { this.id = id; }

    // Getters
    public int       getIdUsuario()     { return idUsuario; }
    public int       getIdCurso()       { return idCurso; }
    public LocalDate getDataInscricao() { return dataInscricao; }
    public int       getEstado()        { return estado; }

    // Setters
    public void setIdUsuario(int idUsuario)         { this.idUsuario = idUsuario; }
    public void setIdCurso(int idCurso)             { this.idCurso = idCurso; }
    public void setDataInscricao(LocalDate data)    { this.dataInscricao = data; }
    public void setEstado(int estado)               { this.estado = estado; }

    public String getEstadoTexto() {
        switch (estado) {
            case ATIVA:     return "Ativa";
            case CANCELADA: return "Cancelada";
            default:        return "Estado desconhecido";
        }
    }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idUsuario);
        dos.writeInt(idCurso);
        dos.writeInt(dataInscricao.getYear());
        dos.writeInt(dataInscricao.getMonthValue());
        dos.writeInt(dataInscricao.getDayOfMonth());
        dos.writeInt(estado);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        id            = dis.readInt();
        idUsuario     = dis.readInt();
        idCurso       = dis.readInt();
        int ano = dis.readInt();
        int mes = dis.readInt();
        int dia = dis.readInt();
        dataInscricao = LocalDate.of(ano, mes, dia);
        estado        = dis.readInt();
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Usuário: %d | Curso: %d | Data: %02d/%02d/%d | Estado: %s",
            id, idUsuario, idCurso,
            dataInscricao.getDayOfMonth(), dataInscricao.getMonthValue(), dataInscricao.getYear(),
            getEstadoTexto());
    }
}
