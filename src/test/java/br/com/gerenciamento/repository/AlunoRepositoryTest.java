package br.com.gerenciamento.repository;

import static org.junit.Assert.assertThat;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.gerenciamento.enums.Curso;
import br.com.gerenciamento.model.Aluno;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AlunoRepositoryTest {
    @Test
    void deveBuscarAlunoPorMatriculaExistente() {
        // Arrange
        Aluno aluno = criarAlunoValido();
        entityManager.persistAndFlush(aluno);
        
        // Act
        Optional<Aluno> encontrado = alunoRepository.findByMatricula("20230001");
        
        // Assert
        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getNome()).isEqualTo("Maria Silva");
    }

    @Test
    void deveRetornarAlunosAtivosPorCurso() {
        // Arrange
        Aluno ativoAdmin = criarAlunoValido();
        Aluno ativoEng = criarAlunoValido();
        ativoEng.setCurso(Curso.ENGENHARIA);
        Aluno inativoAdmin = criarAlunoValido();
        inativoAdmin.setStatus(Status.INATIVO);
        
        entityManager.persist(ativoAdmin);
        entityManager.persist(ativoEng);
        entityManager.persist(inativoAdmin);
        entityManager.flush();
        
        // Act
        List<Aluno> resultado = alunoRepository.findByCursoAndStatus(Curso.ADMINISTRACAO, Status.ATIVO);
        
        // Assert
        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCurso()).isEqualTo(Curso.ADMINISTRACAO);
        assertThat(resultado.get(0).getStatus()).isEqualTo(Status.ATIVO);
    }

    @Test
    void deveRetornarPaginaDeAlunos() {
        // Arrange
        entityManager.persist(criarAlunoValido());
        entityManager.persist(criarAlunoValido());
        entityManager.flush();
        
        // Act
        Page<Aluno> pagina = alunoRepository.findAll(PageRequest.of(0, 5));
        
        // Assert
        assertThat(pagina.getContent()).hasSize(2);
        assertThat(pagina.getTotalElements()).isEqualTo(2);
    }
    
}

