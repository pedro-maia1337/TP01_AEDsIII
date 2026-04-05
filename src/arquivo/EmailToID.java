package arquivo;

import auxiliares.RegistroHashExtensivel;
import java.io.*;

//Email -> ID

public class EmailToID implements RegistroHashExtensivel<EmailToID> {

    private static final int   MAX_EMAIL = 100;
    private static final short TAMANHO   = (short)(MAX_EMAIL + 4);

    private String email;
    private int    id;

    public EmailToID() {
        this.email = "";
        this.id    = -1;
    }

    public EmailToID(String email, int id) {
        this.email = email;
        this.id    = id;
    }

    public String getEmail() { return email; }
    public int    getId()    { return id; }

    @Override public int   hashCode() { return hash(this.email); }
    @Override public short size()     { return TAMANHO; }
    @Override public String toString(){ return "(" + email + ";" + id + ")"; }

    @Override
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        byte[] eb = new byte[MAX_EMAIL];
        byte[] src = email.getBytes("UTF-8");
        System.arraycopy(src, 0, eb, 0, Math.min(src.length, MAX_EMAIL));
        dos.write(eb);
        dos.writeInt(id);
        return baos.toByteArray();
    }

    @Override
    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba);
        DataInputStream dis = new DataInputStream(bais);
        byte[] eb = new byte[MAX_EMAIL];
        dis.readFully(eb);
        int len = 0;
        while (len < eb.length && eb[len] != 0) len++;
        this.email = new String(eb, 0, len, "UTF-8");
        this.id    = dis.readInt();
    }

    public static int hash(String email) {
        if (email == null || email.isEmpty()) return 0;
        long h = 0;
        for (char c : email.toLowerCase().trim().toCharArray())
            h = h * 31 + c;
        return (int) Math.abs(h % (int)(1e9 + 7));
    }
}
