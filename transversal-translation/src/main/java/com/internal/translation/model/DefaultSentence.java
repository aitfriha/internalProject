package com.internal.translation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data()
@AllArgsConstructor()
@NoArgsConstructor()
@Accessors(chain=true)
@Document(collection = "defaultSentence")
@Builder
public class DefaultSentence {
    @Id
    private String _id;

    private String code;

    private String value;

}
