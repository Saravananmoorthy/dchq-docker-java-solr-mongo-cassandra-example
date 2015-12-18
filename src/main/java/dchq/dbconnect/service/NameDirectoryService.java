package dchq.dbconnect.service;

import dchq.dbconnect.model.NameDirectory;

import java.util.List;

/**
 * @since 11/22/2015
 */
public interface NameDirectoryService {

    List<NameDirectory> getAllRows();

    NameDirectory getById(Long id);

    Long addNameDirectory(NameDirectory nd);

    void deleteNameDirectoryById(Long id);

    void deleteAll();
}
