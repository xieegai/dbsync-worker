package com.github.bailaohe.dbsync.subscribe;

import com.github.bailaohe.dbsync.event.payload.RowBatchChanged;
import com.github.bailaohe.repository.AbstractService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public abstract class DBSyncETLHandler<S, I, T> extends DBSyncHandler<S> {
//    @Autowired
//    protected AbstractService<I, S> sourceService;

    @Autowired
    protected AbstractService<I, T> targetService;

    @Getter
    private Class<T> targetEntityClass;

    /**
     * The function to generate the target table data
     * @param payload the payload
     * @param newRows the new rows
     * @return  the target rows
     */
    public abstract List<T> generateTargeList(RowBatchChanged payload, List<S> newRows);

    /**
     * The function to get the key from a primary subscribe row
     * @param entity the entity
     * @return the key
     */
    public abstract I getKey(S entity);


    @Override
    @PostConstruct
    public void init() {
        super.init();

        Class subClass = getClass();
        while (!(subClass.getGenericSuperclass() instanceof ParameterizedType)
                && subClass.getSuperclass() != DBSyncETLHandler.class) {
            subClass = subClass.getSuperclass();
        }
        targetEntityClass = (Class<T>) ((ParameterizedType)subClass.getGenericSuperclass()).getActualTypeArguments()[2];
    }

    public Class<S> getSourceEntityClass() {
        return subscribeEntityClass;
    }


    @Override
    public void processInsert(RowBatchChanged payload, List<S> newRows) {
        List<T> targetList = generateTargeList(payload, newRows);
        if (!CollectionUtils.isEmpty(targetList)) {
            targetService.insertList(targetList);
        }
    }

    @Override
    public void processDelete(RowBatchChanged payload, List<S> oldRows) {
        List<I> instIds = oldRows.stream().map(x -> getKey(x)).collect(Collectors.toList());
        targetService.deleteByIds(instIds);
    }

    @Override
    public void processUpdate(RowBatchChanged payload, List<S> oldRows, List<S> newRows) {
        processDelete(payload, oldRows);
        if (!CollectionUtils.isEmpty(newRows)) {
            processInsert(payload, newRows);
        }
    }

    @Override
    public void processSecondaryInsert(RowBatchChanged payload, List newSecondaryRows) {
        processSecondaryUpdate(payload, newSecondaryRows, newSecondaryRows);
    }

    @Override
    public void processSecondaryDelete(RowBatchChanged payload, List oldSecondaryRows) {
        processSecondaryUpdate(payload, oldSecondaryRows, oldSecondaryRows);
    }

//    public void initTargetTable() {
//        int pageSize = 64;
//        int totalCount = sourceService.count();
//        int totalPage = Double.valueOf(Math.ceil(Double.valueOf(totalCount) / pageSize)).intValue();
//
//        for (int pageNo = 0; pageNo < totalPage; pageNo++) {
//            List<S> sourceList = sourceService.findByQueryPage(new EmptyQuery<>(getSourceEntityClass()), pageNo, pageSize);
//            List<T> targetList = generateTargeList(null, sourceList);
//            targetService.insertIfNotExist(targetList);
//            log.info("{}/{} inserted", Math.min((pageNo + 1) * pageSize, totalCount), totalCount);
//        }
//    }
}
