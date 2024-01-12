package com.app.toaster.controller.request.category;

import java.util.ArrayList;

public record EditCategoryRequestDto (
        ArrayList<ChangeCateoryTitleDto> changeCategoryTitleList,
        ArrayList<ChangeCateoryPriorityDto> changeCategoryPriorityList){
}
