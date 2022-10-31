package com.hegde.springbootjdbc.demo.backup;

import org.springframework.batch.item.ItemProcessor;

public class GeometryItemProcessor implements ItemProcessor<GeometryObjectsModel, GeometryObjectsModel> {


    @Override
    public GeometryObjectsModel process(GeometryObjectsModel geometryObjectsModel) throws Exception {
        return geometryObjectsModel;
    }
}
