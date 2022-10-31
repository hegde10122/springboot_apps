package com.hegde.springbootjdbc.demo.backup;

import com.vividsolutions.jts.geom.Geometry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data // from lombok
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class GeometryObjectsModel {

    @Column(columnDefinition = "SHAPE")
    private Geometry shape;

    @Id
    private Long gid;
}
