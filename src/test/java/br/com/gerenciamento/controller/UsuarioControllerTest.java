package br.com.gerenciamento.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Usuario;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsuarioControllerTest {
    @Test
    void deveRetornarUsuarioPorEmail() throws Exception {
        Usuario usuario = new Usuario("teste@email.com", "senha123");
        when(usuarioService.buscarPorEmail("teste@email.com")).thenReturn(usuario);

        mockMvc.perform(get("/usuarios?email=teste@email.com"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email").value("teste@email.com"));
    }

    @Test
    void deveRetornar404ParaUsuarioInexistente() throws Exception {
        when(usuarioService.buscarPorEmail("inexistente@email.com"))
            .thenThrow(RuntimeException.class);

        mockMvc.perform(get("/usuarios?email=inexistente@email.com"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarUsuario() throws Exception {
        Usuario usuario = new Usuario("novo@email.com", "senha");
        when(usuarioService.salvar(any(Usuario.class))).thenReturn(usuario);

        mockMvc.perform(post("/usuarios")
               .contentType("application/json")
               .content("{\"email\":\"novo@email.com\",\"senha\":\"senha\"}"))
               .andExpect(status().isCreated());
    }

    @Test
    void deveRecusarUsuarioSemSenha() throws Exception {
        mockMvc.perform(post("/usuarios")
               .contentType("application/json")
               .content("{\"email\":\"invalido@email.com\"}"))
               .andExpect(status().isBadRequest());
    }

}
