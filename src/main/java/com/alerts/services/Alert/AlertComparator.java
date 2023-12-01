package com.alerts.services.Alert;

import java.util.Comparator;

import com.alerts.entities.Alert;
import com.alerts.enums.AlertType;

public class AlertComparator implements Comparator<Alert> {

    @Override
    public int compare(Alert alert1, Alert alert2) {
        // Si el 1ro va antes que el 2do, retorno un NEGATIVO
        // Si el 1ro va después del 2do, retorno un POSITIVO
        // Si son iguales retorno un CERO
        if (alert1.getAlertType() == AlertType.URGENT && alert2.getAlertType() == AlertType.INFORMATIVE) {
            // La alerta urgente siempre va primero
            return -1;
        } else if (alert1.getAlertType() == AlertType.INFORMATIVE && alert2.getAlertType() == AlertType.URGENT) {
            // La alerta urgente siempre va primero
            return 1;
        } else if (alert1.getAlertType() == AlertType.URGENT && alert2.getAlertType() == AlertType.URGENT) {
            return -1;
        } else {
            // Ambas son Informativas, las deja iguales (FIFO)
            return 0;
        }
    }
}
