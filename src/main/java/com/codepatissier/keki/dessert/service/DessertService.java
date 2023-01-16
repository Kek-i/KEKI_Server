package com.codepatissier.keki.dessert.service;

import com.codepatissier.keki.common.BaseException;
import com.codepatissier.keki.dessert.dto.GetStoreDessertsRes;
import com.codepatissier.keki.dessert.repository.DessertRepository;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.store.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.codepatissier.keki.common.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
public class DessertService {
    private final DessertRepository dessertRepository;
    private final StoreRepository storeRepository;

    public GetStoreDessertsRes getDessertList(Long storeIdx, Long cursorIdx, Pageable page) throws BaseException {
        try {
            Store store = storeRepository.findById(storeIdx)
                    .orElseThrow(() -> new BaseException(INVALID_STORE_IDX));

            List<GetStoreDessertsRes.Dessert> dessertList = cursorIdx == null ?
                    getDessertList(store, page) :
                    getDessertListWithCursor(store, cursorIdx, page);

            Long lastIdxOfList = getLastIdxOfList(dessertList);

            return new GetStoreDessertsRes(dessertList, lastIdxOfList, hasNext(lastIdxOfList));
        } catch (BaseException e) {
            throw e;
        } catch (Exception e) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    private List<GetStoreDessertsRes.Dessert> getDessertList(Store store, Pageable page) {
        return dessertRepository.findByStoreOrderByDessertIdxDesc(store, page).stream()
                .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                        dessert.getDessertImg())).collect(Collectors.toList());
    }

    private List<GetStoreDessertsRes.Dessert> getDessertListWithCursor(Store store, Long cursorIdx, Pageable page) throws BaseException {
        dessertRepository.findById(cursorIdx).orElseThrow(() -> new BaseException(INVALID_DESSERT_IDX));

        return dessertRepository.findByStoreAndDessertIdxLessThanOrderByDessertIdxDesc(store, cursorIdx, page).stream()
                .map(dessert -> new GetStoreDessertsRes.Dessert(dessert.getDessertIdx(),
                        dessert.getDessertImg())).collect(Collectors.toList());
    }

    private static Long getLastIdxOfList(List<GetStoreDessertsRes.Dessert> dessertList) {
        return dessertList.isEmpty() ? null : dessertList.get(dessertList.size() - 1).getDessertIdx();
    }

    private Boolean hasNext(Long lastIdx) {
        return lastIdx != null && this.dessertRepository.existsByDessertIdxLessThan(lastIdx);
    }
}
