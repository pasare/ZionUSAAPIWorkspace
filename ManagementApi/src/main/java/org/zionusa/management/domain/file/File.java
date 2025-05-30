package org.zionusa.management.domain.file;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Audited
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "files")
public class File {

    /**
     * Base Properties
     */

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean archived = false;

    @NotNull
    @Column(columnDefinition = "bit default 0")
    private Boolean hidden = false;

    /**
     * Properties
     */
    @JsonIgnore
    @Lob
    @NotNull
    @NotAudited
    @Column(name = "data", columnDefinition = "varbinary(max)")
    private byte[] data;

    private String displayName;

    private String name;

    private String type;

    public File(String displayName, String name, String type, byte[] data) {
        this.data = data;
        this.displayName = displayName;
        this.name = name;
        this.type = type;
    }
}
