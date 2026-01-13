package com.prueba_proteccion.prueba.repositories.tickets;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.prueba_proteccion.prueba.dto.tickets.PageTickets;
import com.prueba_proteccion.prueba.utils.MapperRepository;
import com.prueba_proteccion.prueba.utils.PageableDto;

@Repository
public class TicketsQueryRepository {
        @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

public PageImpl<PageTickets> pageTickets(PageableDto<Object> pageableDto) {

    int pageNumber = pageableDto.getPage() != null
            ? pageableDto.getPage().intValue()
            : 0;

    int pageSize = pageableDto.getRows() != null
            ? pageableDto.getRows().intValue()
            : 10;

    String search = pageableDto.getSearch() != null
            ? pageableDto.getSearch().trim()
            : null;

    String sql = """
        SELECT
            t.id,
            t.tipo as type,
            t.prioridad_manual as priority,
            t.fecha_creation as date_creation,
            u.id AS user_id,
            u.username,
            (
                CASE t.tipo
                    WHEN 'INCIDENTE' THEN 100
                    WHEN 'REQUERIMIENTO' THEN 50
                    WHEN 'CONSULTA' THEN 10
                END
                + t.prioridad_manual
                + EXTRACT(EPOCH FROM (NOW() - t.fecha_creation)) / 3600
            )::INT AS priority_final,
            COUNT(*) OVER() AS total_rows
        FROM tickets t
        INNER JOIN users u ON u.id = t.user_id
        WHERE t.deleted_at IS NULL
          AND u.activo = 1
        """;

    MapSqlParameterSource params = new MapSqlParameterSource();
    if (search != null && !search.isEmpty()) {
        sql += """
            AND (
                LOWER(t.tipo) ILIKE :search
                OR LOWER(u.username) ILIKE :search
            )
        """;
        params.addValue("search", "%" + search.toLowerCase() + "%");
    }
    if (pageableDto.getOrder_by() != null && !pageableDto.getOrder_by().isEmpty()) {
        sql += " ORDER BY " + pageableDto.getOrder_by() + " " + pageableDto.getOrder() + " ";
    } else {
        sql += " ORDER BY priority_final DESC ";
    }
    sql += " OFFSET :offset LIMIT :limit ";

    long offset = (long) pageNumber * pageSize;
    params.addValue("offset", offset);
    params.addValue("limit", pageSize);
    List<Map<String, Object>> resultList =
            namedParameterJdbcTemplate.query(
                    sql,
                    params,
                    new ColumnMapRowMapper()
            );
    List<PageTickets> result =
            MapperRepository.mapListToDtoList(
                    resultList,
                    PageTickets.class
            );

    long count = resultList.isEmpty()
            ? 0
            : ((Number) resultList.get(0).get("total_rows")).longValue();

    PageRequest pageable = PageRequest.of(pageNumber, pageSize);

    return new PageImpl<>(result, pageable, count);
}

}
