package br.com.gerenciamento.service;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.enums.Status;
import br.com.gerenciamento.enums.Turno;
import br.com.gerenciamento.model.Aluno;
import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoServiceTest {

    @Autowired
    private ServiceAluno serviceAluno;

    @Test
    public void getById() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("Vinicius");
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        this.serviceAluno.save(aluno);

        Aluno alunoRetorno = this.serviceAluno.getById(1L);
        Assert.assertTrue(alunoRetorno.getNome().equals("Vinicius"));
    }

    @Test
    public void salvarSemNome() {
        Aluno aluno = new Aluno();
        aluno.setId(1L);
        aluno.setTurno(Turno.NOTURNO);
        aluno.setCurso(Curso.ADMINISTRACAO);
        aluno.setStatus(Status.ATIVO);
        aluno.setMatricula("123456");
        Assert.assertThrows(ConstraintViolationException.class, () -> {
                this.serviceAluno.save(aluno);});
    }
    // Teste 1: Buscar aluno por ID existente
    @Test
    void deveRetornarAlunoQuandoIdExistir() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(alunoValido));
        
        Aluno resultado = alunoService.getById(1L);
        
        assertEquals("Vinicius", resultado.getNome());
        assertEquals(Turno.NOTURNO, resultado.getTurno());
        verify(alunoRepository, times(1)).findById(1L);
    }

    // Teste 2: Lançar exceção quando aluno não existe
    @Test
    void deveLancarExcecaoQuandoIdNaoExistir() {
        when(alunoRepository.findById(99L)).thenReturn(Optional.empty());
        
        assertThrows(RuntimeException.class, () -> {
            alunoService.getById(99L);
        });
    }

    // Teste 3: Salvar aluno com sucesso
    @Test
    void deveSalvarAlunoQuandoDadosValidos() {
        when(alunoRepository.save(any(Aluno.class))).thenReturn(alunoValido);
        
        Aluno resultado = alunoService.save(alunoValido);
        
        assertNotNull(resultado);
        assertEquals("123456", resultado.getMatricula());
        verify(alunoRepository, times(1)).save(alunoValido);
    }

    // Teste 4: Falhar ao salvar aluno inválido
    @Test
    void deveLancarExcecaoQuandoAlunoInvalido() {
        Aluno alunoInvalido = new Aluno(); // Sem nome (obrigatório)
        
        when(alunoRepository.save(alunoInvalido))
            .thenThrow(ConstraintViolationException.class);
        
        assertThrows(ConstraintViolationException.class, () -> {
            alunoService.save(alunoInvalido);
        });
    }

}