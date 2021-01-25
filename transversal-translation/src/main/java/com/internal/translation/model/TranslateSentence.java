package com.internal.translation.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
@Document(collection = "translateSentence")
public class TranslateSentence {

    @Id
    @EqualsAndHashCode.Exclude
    private String _id;

    private String countryLanguageCode;

    @DBRef
    private DefaultSentence defaultSentence;

    @EqualsAndHashCode.Exclude
    private String translation;
}
