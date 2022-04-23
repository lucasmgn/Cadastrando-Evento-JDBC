import com.curso.eventos.dao.EventoDAO;
import com.curso.eventos.model.Evento;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

public class EventoDAOTest {

    private static Connection connection;

    @BeforeClass
    public static void iniciarClasse() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cadastroevento" +
                "?useTimezone=true&serverTimezone=UTC", "root", "auhpuk260202");
    }

    @AfterClass
    public static void encerrarClasse() throws SQLException {
        connection.close();
    }

    @Test
    public void crud() {
        Evento evento = new Evento(null, "Notebook", new Date());

        // Criando a instância do DAO.
        EventoDAO dao = new EventoDAO(connection);

        // Fazendo a inserção e recuperando o identificador.
        Integer id = dao.salvar(evento);
        Assert.assertNotNull("Identificador foi retornado como NULL.", id);

        // Atribuindo o identificador retornado ao atributo "id".
        evento.setId(id);

        // Verificando se o registro realmente foi para o banco de dados.
        evento = dao.buscar(evento.getId());
        Assert.assertNotNull("Evento nulo.", evento);

        // Atualizando o registro no banco de dados.
        String nomeAlterado = evento.getNome() + " alterado";
        evento.setNome(nomeAlterado);
        dao.atualizar(evento);

        // Verificando se atualização ocorreu com sucesso.
        evento = dao.buscar(evento.getId());
        Assert.assertEquals("O nome não foi atualizado corretamente.", nomeAlterado, evento.getNome());

        // Removendo o registro.
        dao.deletar(evento.getId());

        // O registro não existe mais. O método "buscar" deve retornar nulo.
        evento = dao.buscar(evento.getId());
        Assert.assertNull("Evento ainda existe e não deveria.", evento);
    }
}
