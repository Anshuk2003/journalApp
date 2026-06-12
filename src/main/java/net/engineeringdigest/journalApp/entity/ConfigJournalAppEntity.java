package net.engineeringdigest.journalApp.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config-journal-app")
@Data
@NoArgsConstructor
public class ConfigJournalAppEntity {
private String key;
private String value;
}
