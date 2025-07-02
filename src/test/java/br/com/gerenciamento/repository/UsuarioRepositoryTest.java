package br.com.gerenciamento.repository;

import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioRepositoryTest {
    @Test
    void deveSalvarUsuarioComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setSenha("senha123");

        // Act
        Usuario salvo = usuarioRepository.save(usuario);
        
        // Assert
        assertThat(salvo.getId()).isNotNull();
        assertThat(salvo.getEmail()).isEqualTo("teste@email.com");
    }

    @Test
    void deveBuscarUsuarioPorEmailExistente() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("buscar@email.com");
        usuario.setSenha("senha123");
        entityManager.persistAndFlush(usuario);

        // Act
        Optional<Usuario> encontrado = usuarioRepository.findByEmail("buscar@email.com");

        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getEmail()).isEqualTo("buscar@email.com");
    }

    @Test
    void deveRetornarVazioParaEmailInexistente() {
        // Act
        Optional<Usuario> resultado = usuarioRepository.findByEmail("inexistente@email.com");

        // Assert
        assertThat(resultado).isEmpty();
    }

    @Test
    void deveVerificarExistenciaPorEmail() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("existente@email.com");
        usuario.setSenha("senha123");
        entityManager.persistAndFlush(usuario);

        // Act
        boolean existe = usuarioRepository.existsByEmail("existente@email.com");
        boolean naoExiste = usuarioRepository.existsByEmail("outro@email.com");

        // Assert
        assertThat(existe).isTrue();
        assertThat(naoExiste).isFalse();
    }

    @Test
    void deveDeletarUsuario() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("deletar@email.com");
        usuario.setSenha("senha123");
        entityManager.persistAndFlush(usuario);

        // Act
        usuarioRepository.delete(usuario);
        Optional<Usuario> deletado = usuarioRepository.findByEmail("deletar@email.com");

        // Assert
        assertThat(deletado).isEmpty();
    }
}
