package entidades;

import auxiliares.Registro;
import java.io.*;
import java.time.LocalDate;
import java.util.Random;

public class Curso implements Registro {
    
    private int id;
    private int idUsuario; // Chave estrangeira
    private String nome;
    private LocalDate dataInicio;
    private String descricao;
    private String codigoCompartilhavel;
    private int estado; // 0=ativo recebendo inscrições, 1=ativo sem novas inscrições, 2=concluído, 3=cancelado
    
    public static final int MAX_NOME = 200;
    public static final int MAX_DESCRICAO = 1000;
    public static final int CODIGO_SIZE = 10;
    
    // Constantes para estados
    public static final int ATIVO_INSCRICOES = 0;
    public static final int ATIVO_SEM_INSCRICOES = 1;
    public static final int CONCLUIDO = 2;
    public static final int CANCELADO = 3;
    
    public Curso() {
        this(-1, -1, "", LocalDate.now(), "", gerarCodigoCompartilhavel(), ATIVO_INSCRICOES);
    }
    
    public Curso(int idUsuario, String nome, LocalDate dataInicio, String descricao) {
        this(-1, idUsuario, nome, dataInicio, descricao, gerarCodigoCompartilhavel(), ATIVO_INSCRICOES);
    }
    
    public Curso(int id, int idUsuario, String nome, LocalDate dataInicio, String descricao, 
                 String codigoCompartilhavel, int estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.descricao = descricao;
        this.codigoCompartilhavel = codigoCompartilhavel;
        this.estado = estado;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void setId(int id) {
        this.id = id;
    }
    
    // Getters
    public int getIdUsuario() { return idUsuario; }
    public String getNome() { return nome; }
    public LocalDate getDataInicio() { return dataInicio; }
    public String getDescricao() { return descricao; }
    public String getCodigoCompartilhavel() { return codigoCompartilhavel; }
    public int getEstado() { return estado; }
    
    // Setters
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setNome(String nome) { this.nome = nome; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setCodigoCompartilhavel(String codigo) { this.codigoCompartilhavel = codigo; }
    public void setEstado(int estado) { this.estado = estado; }
    
    // Gera um código alfanumérico de 10 caracteres seguindo o padrão NanoID
    private static String gerarCodigoCompartilhavel() {
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder codigo = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < CODIGO_SIZE; i++) {
            codigo.append(chars.charAt(random.nextInt(chars.length())));
        }
        return codigo.toString();
    }
    
    public String getEstadoTexto() {
        switch (estado) {
            case ATIVO_INSCRICOES:
                return "Ativo e recebendo inscrições";
            case ATIVO_SEM_INSCRICOES:
                return "Ativo, mas sem novas inscrições";
            case CONCLUIDO:
                return "Realizado e concluído";
            case CANCELADO:
                return "Cancelado";
            default:
                return "Estado desconhecido";
        }
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(id);
        dos.writeInt(idUsuario);
        dos.writeUTF(nome);
        dos.writeInt(dataInicio.getYear());
        dos.writeInt(dataInicio.getMonthValue());
        dos.writeInt(dataInicio.getDayOfMonth());
        dos.writeUTF(descricao);
        dos.writeUTF(codigoCompartilhavel);
        dos.writeInt(estado);
        return baos.toByteArray();
    }
    
    @Override
    public void fromByteArray(byte[] b) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(b);
        DataInputStream dis = new DataInputStream(bais);
        id = dis.readInt();
        idUsuario = dis.readInt();
        nome = dis.readUTF();
        int ano = dis.readInt();
        int mes = dis.readInt();
        int dia = dis.readInt();
        dataInicio = LocalDate.of(ano, mes, dia);
        descricao = dis.readUTF();
        codigoCompartilhavel = dis.readUTF();
        estado = dis.readInt();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d\nUsuário: %d\nNome: %s\nData de Início: %02d/%02d/%d\nDescrição: %s\nCódigo: %s\nEstado: %s",
            id, idUsuario, nome, dataInicio.getDayOfMonth(), dataInicio.getMonthValue(), 
            dataInicio.getYear(), descricao, codigoCompartilhavel, getEstadoTexto());
    }
}