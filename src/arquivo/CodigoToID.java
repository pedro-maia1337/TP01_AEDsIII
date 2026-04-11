package arquivo;

import auxiliares.RegistroHashExtensivel;
import java.io.*;

public class CodigoToID implements RegistroHashExtensivel<CodigoToID> {
    private String codigo;
    private int id;
    private short TAMANHO = 34; // 4 bytes int + 30 bytes string
    
    public CodigoToID() {
        this.codigo = "";
        this.id = -1;
    }
    
    public CodigoToID(String codigo, int id) {
        this.codigo = codigo;
        this.id = id;
    }
    
    public String getCodigo() { return codigo; }
    public int getId() { return id; }
    
    @Override
    public int hashCode() {
        return hash(this.codigo);
    }
    
    public static int hash(String codigo) {
        return Math.abs(codigo.hashCode());
    }
    
    @Override
    public short size() {
        return TAMANHO;
    }
    
    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(id);
        dos.writeUTF(codigo);
        
        byte[] resultado = baos.toByteArray();

        if (resultado.length < TAMANHO) {
            byte[] padded = new byte[TAMANHO];
            System.arraycopy(resultado, 0, padded, 0, resultado.length);
            return padded;
        }
        
        return resultado;
    }
    
    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        
        id = dis.readInt();
        codigo = dis.readUTF();
    }
    
    @Override
    public String toString() {
        return "Codigo: " + codigo + " | ID: " + id;
    }
}