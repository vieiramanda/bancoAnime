import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.List;

public class EpisodioDAO {
    private Connection con;

    public EpisodioDAO() {
        this.con = ConexaoSQLite.conectar();
    }

    public void create(Episodio ep) {

        PreparedStatement stmt = null;

        try {
            // formação de statements a partir do objeto passado como parâmetro, para
            // executar o sql
            stmt = con.prepareStatement(
                    "INSERT INTO episodio (pk_id_ep,titulo_ep,duracao_ep,fk_temporada_id)VALUES(?,?,?,?)");
            // a seguir, captura de dados do objeto para o statement
            stmt.setInt(1, ep.getId());
            stmt.setString(2, ep.getTitulo());
            stmt.setString(3, ep.getDuracao());
            stmt.setInt(4, ep.getFktemp().getId()); // inserção da fk com valor igual ao id da respectiva temporada ->
                                                    // veja classe Episodio.java**

            stmt.executeUpdate(); // executa a inserção
            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar! " + e.getMessage());
        } finally {
            ConexaoSQLite.desconectar(con, stmt);
        }

    }

    // listagem de episódios
    public List<Episodio> readEpisodio(Temporada temp) { // retorna um array de episodios que sao listados no read() de
                                                         // AnimeDAO
        PreparedStatement stmt = null;
        ResultSet rs = null;

        List<Episodio> episodios = new ArrayList<>();

        try {
            // consulta INNER JOIN: retorna a linha da tabela episodio que possui a
            // fk.episodio == id.temporada
            stmt = con.prepareStatement(
                    "SELECT * FROM episodio INNER JOIN temporada ON episodio.fk_temporada_id = temporada.pk_id_temp");
            rs = stmt.executeQuery();

            while (rs.next()) { // percorre as tabelas que atendem à consulta acima
                int idTemporada = rs.getInt("pk_id_ep");
                String titulo = rs.getString("titulo_ep");
                String duracao = rs.getString("duracao_ep");

                Episodio novoEp = new Episodio(idTemporada, titulo, duracao, temp);
                episodios.add(novoEp); // add ao array q será retornado
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao salvar! " + e.getMessage());
        } finally {
            ConexaoSQLite.desconectar(con, stmt, rs);
        }
        // array retornado à listagem dos Animes
        return episodios;
    }
    // outros metodos: update(), delete()

}