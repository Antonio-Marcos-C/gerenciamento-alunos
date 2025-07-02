package br.com.gerenciamento.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.model.Aluno;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoControllerTest {
    @Test
    void deveRetornarAlunoPorId() throws Exception {
        Aluno aluno = new Aluno(1L, "Maria Silva");
        when(alunoService.buscarPorId(1L)).thenReturn(aluno);

        mockMvc.perform(get("/alunos/1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nome").value("Maria Silva"));
    }

    @Test
    void deveRetornar404ParaAlunoInexistente() throws Exception {
        when(alunoService.buscarPorId(99L)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/alunos/99"))
               .andExpect(status().isNotFound());
    }

    @Test
    void deveCriarAluno() throws Exception {
        Aluno aluno = new Aluno(1L, "João Santos");
        when(alunoService.salvar(any(Aluno.class))).thenReturn(aluno);

        mockMvc.perform(post("/alunos")
               .contentType("application/json")
               .content("{\"nome\":\"João Santos\"}"))
               .andExpect(status().isCreated());
    }

    @Test
    void deveDeletarAluno() throws Exception {
        doNothing().when(alunoService).deletar(1L);

        mockMvc.perform(delete("/alunos/1"))
               .andExpect(status().isNoContent());
    }

}
