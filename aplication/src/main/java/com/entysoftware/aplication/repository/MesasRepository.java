package com.entysoftware.aplication.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.entysoftware.aplication.model.Mesas;
import java.util.List;



public interface MesasRepository extends JpaRepository<Mesas,Integer>{
    @Query("SELECT m FROM Mesas m WHERE m.FK_id_establecimiento = :idEstablecimiento")
    List<Mesas> findByFK_id_establecimiento(@Param("idEstablecimiento") int fK_id_establecimiento);
}
