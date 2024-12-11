package com.spot.fun.usr.custom.domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "tbl_custom")
@AllArgsConstructor
@Builder
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Custom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cno;

    private String title;
    private String description;
    private String tags;

    @OneToMany(mappedBy = "custom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomPlace> customPlaces = new ArrayList<>();

    @PostLoad
    public void splitTags() {
        if (tags != null && !tags.isBlank()) {
            this.tagList = List.of(tags.split(","));
        }
    }

    @Transient
    private List<String> tagList = new ArrayList<>();

    public List<String> getTagList() {
        if (tags == null || tags.isBlank()) {
            return new ArrayList<>();
        }
        return List.of(tags.split(","));
    }

    public void setTagList(List<String> tags) {
        this.tags = String.join(",", tags);
    }

    public void addCustomPlace(CustomPlace customPlace) {
        customPlaces.add(customPlace);
    }
    
}

