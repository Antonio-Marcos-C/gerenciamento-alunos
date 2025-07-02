package br.com.gerenciamento.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioServiceTest {
     // Teste 1: Deve salvar usuário com sucesso
    @Test
    void deveSalvarUsuarioComSucesso() {
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario resultado = usuarioService.salvar(usuario);

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getEmail());
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(usuario);
    }

    // Teste 2: Deve lançar exceção quando email já existe
    @Test
    void deveLancarExcecaoQuandoEmailExistente() {
        when(usuarioRepository.existsByEmail("teste@email.com")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            usuarioService.salvar(usuario);
        });

        verify(usuarioRepository, never()).save(any());
    }

    // Teste 3: Deve encontrar usuário por email
    @Test
    void deveEncontrarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("teste@email.com"))
            .thenReturn(Optional.of(usuario));

        Usuario encontrado = usuarioService.buscarPorEmail("teste@email.com");

        assertNotNull(encontrado);
        assertEquals(1L, encontrado.getId());
    }

    // Teste 4: Deve lançar exceção quando usuário não encontrado por email
    @Test
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        when(usuarioRepository.findByEmail("inexistente@email.com"))
            .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorEmail("inexistente@email.com");
        });
    }
}
