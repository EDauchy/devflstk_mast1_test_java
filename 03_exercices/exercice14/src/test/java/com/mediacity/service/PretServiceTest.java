package com.mediacity.service;

import com.mediacity.exception.OuvrageDejaEmprunteException;
import com.mediacity.model.Pret;
import com.mediacity.model.StatutPret;
import com.mediacity.repository.PretRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PretServiceTest {

    private static final LocalDate DATE_EMPRUNT = LocalDate.of(2026, 6, 1);

    @Mock
    private PretRepository pretRepository;

    @InjectMocks
    private PretService pretService;

    @Test
    void devraitCreerUnPret_avecDateDeRetourDans21Jours() {
        // Arrange
        when(pretRepository.findPretEnCoursParOuvrage("OUV-001")).thenReturn(Optional.empty());
        when(pretRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Pret pret = pretService.creerPret("ADH-001", "OUV-001", DATE_EMPRUNT);

        // Assert
        assertThat(pret.adherentId()).isEqualTo("ADH-001");
        assertThat(pret.ouvrageId()).isEqualTo("OUV-001");
        assertThat(pret.dateEmprunt()).isEqualTo(DATE_EMPRUNT);
        assertThat(pret.dateRetourPrevue()).isEqualTo(DATE_EMPRUNT.plusDays(21));
        assertThat(pret.statut()).isEqualTo(StatutPret.EN_COURS);
        assertThat(pret.dateRetourEffective()).isNull();

        ArgumentCaptor<Pret> captor = ArgumentCaptor.forClass(Pret.class);
        verify(pretRepository).save(captor.capture());
        assertThat(captor.getValue().dateRetourPrevue()).isEqualTo(LocalDate.of(2026, 6, 22));
    }

    @Test
    void devraitRefuserUnPret_quandOuvrageEstDejaEmprunte() {
        // Arrange
        Pret pretExistant = new Pret(
                "PRET-1",
                "ADH-002",
                "OUV-001",
                DATE_EMPRUNT,
                DATE_EMPRUNT.plusDays(21),
                null,
                StatutPret.EN_COURS
        );
        when(pretRepository.findPretEnCoursParOuvrage("OUV-001")).thenReturn(Optional.of(pretExistant));

        // Act + Assert
        assertThatThrownBy(() -> pretService.creerPret("ADH-001", "OUV-001", DATE_EMPRUNT))
                .isInstanceOf(OuvrageDejaEmprunteException.class)
                .hasMessage("L'ouvrage OUV-001 est déjà emprunté");

        verify(pretRepository, never()).save(any());
    }
}
