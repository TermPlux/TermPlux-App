cmake_minimum_required(VERSION 3.22.1)

project("termplux")

add_library(
        termplux
        SHARED
        termplux-lib.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        termplux
        ${log-lib}
)