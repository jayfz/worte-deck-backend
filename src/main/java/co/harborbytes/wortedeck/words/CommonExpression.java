package co.harborbytes.wortedeck.words;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "common_expression")
public class CommonExpression extends Word {
}
